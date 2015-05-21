package com.eisgroup.notification_manager.service.impl;

import com.eisgroup.notification_manager.dao.MessageDAO;
import com.eisgroup.notification_manager.model.MessageProvider;
import com.eisgroup.notification_manager.model.MessageStatus;
import com.eisgroup.notification_manager.model.SMSMessage;
import com.eisgroup.notification_manager.model.User;
import com.eisgroup.notification_manager.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("smsService")
public class SmsServiceImpl implements SmsService {

    @Autowired
    MessageDAO messageDAO;

    @Autowired
    MessageProvider messageProvider;

    private Map<String, String> errorCodes;
    private Map<String, String> connectionCodes;
    private DocumentBuilderFactory dbf;


    @Override
    public List<SMSMessage> getAllSmsByUser(User user) {
        return messageDAO.getAllSmsByUser(user);
    }

    @Override
    public void sendMultipleSMSMessage(SMSMessage newSMS, List<User> selectedUsers) {
        if (selectedUsers == null) {
            return;
        }
        for (User user : selectedUsers) {
            newSMS.setId(null);
            newSMS.setUser(user);
            newSMS.setAddress(user.getMobileNumber());
            sendSMSMessage(newSMS);
        }
    }

    @Override
    public void checkSMSState(SMSMessage message) {
        if (message.getId() == null) {
            return;
        }

        String xmlString = generateXmlForReport(message);
        try {
            String xmlReport = sendXML(xmlString);
            message = generateReport(message, xmlReport);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            message.setStatus(MessageStatus.FAIL);
            message.setState(e.getMessage());
        }

        messageDAO.update(message);
    }

    @Override
    public void sendSMSMessage(SMSMessage message) {
        message.setStatus(MessageStatus.NEW);
        message.setDispatchDay(new Date());
        String xmlString = generateXmlForSMS(message);

        System.out.println(xmlString);

//        try {
//            String xmlReport = sendXML(xmlString);
//            message = generateReport(message, xmlReport);
//
//        } catch (IOException | ParserConfigurationException | SAXException e) {
//            message.setStatus(MessageStatus.FAIL);
//            message.setState(e.getMessage());
//        }
//        messageDAO.create(message);
    }

    private SMSMessage generateReport(SMSMessage message, String xmlReport) throws IOException, SAXException, ParserConfigurationException {
        if (message.getSmsId() == null || message.getSmsId().isEmpty()) {
            String smsId = getXmlValue("package:message:msg:sms_id", xmlReport);
            message.setSmsId(smsId);
        }
        String smsStatus = getXmlValue("package:message:msg", xmlReport);

        if (smsStatus == null || smsStatus.isEmpty()) {
            smsStatus = getXmlValue("package:status:msg", xmlReport);
        }

        if (smsStatus == null || smsStatus.isEmpty()) {
            smsStatus = getXmlValue("package:error", xmlReport);
        }

        if ("201".equals(smsStatus)) {
            return message;
        }

        if (errorCodes.containsKey(smsStatus)) {
            message.setStatus(MessageStatus.FAIL);
            message.setState(smsStatus + " " + errorCodes.get(smsStatus));
            return message;
        } else if ("102".equals(smsStatus)) {
            message.setStatus(MessageStatus.DELIVERED);
        } else if ("1".equals(smsStatus)) {
            message.setStatus(MessageStatus.SENT);
        } else {
            message.setStatus(MessageStatus.SENDING);
        }
        message.setState(smsStatus + " " + connectionCodes.get(smsStatus));
        return message;
    }


    private String sendXML(String xmlString) throws IOException {
        HttpsURLConnection connection = null;
        String result = "";

        String urlString = messageProvider.getSmsProviderURL();

        try {
            URL url = new URL(urlString);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml; charset=utf-8");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            try (DataOutputStream os = new DataOutputStream(connection.getOutputStream())) {
                os.writeBytes(xmlString);
                os.flush();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    while (reader.ready()) {
                        result += reader.readLine();
                    }
                }
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;

    }

    private String getXmlValue(String var, String xmlReport) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(xmlReport)));
        String[] nodeNames = var.split(":");
        if (nodeNames.length == 0) {
            return null;
        }

        Node neededNode = doc;
        String variable = null;
        for (String nodeName : nodeNames) {
            NodeList nodeList = neededNode.getChildNodes();
            variable = nodeName;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (nodeName.equals(node.getNodeName())) {
                    variable = null;
                    neededNode = node;
                    break;
                }
            }
        }
        String result = "";
        if (variable == null) {
            if (neededNode.getFirstChild() != null) {
                result = neededNode.getFirstChild().getNodeValue();
            }
        } else {
            if (neededNode.getAttributes().getNamedItem(variable) != null) {
                result = neededNode.getAttributes().getNamedItem(variable).getNodeValue();
            }
        }
        return result;
    }

    public SmsServiceImpl() {
        dbf = DocumentBuilderFactory.newInstance();
        errorCodes = new HashMap<>();
        connectionCodes = new HashMap<>();
        connectionCodes.put("1", "Delivered to Provider");
        connectionCodes.put("102", "Message is delivered to destination");
        connectionCodes.put("110", "Message in process of transferring to mobile network");

        errorCodes.put("101", "The message is in enroute state");
        errorCodes.put("103", "Message validity period has expired");
        errorCodes.put("104", "Message has been deleted");
        errorCodes.put("105", "Message is undeliverable");
        errorCodes.put("106", "Message is in accepted state (i.e. has been manually read on behalf of the subscriber by customer service)");
        errorCodes.put("107", "Message is in invalid state The message state is unknown.");
        errorCodes.put("108", "Message is in a rejected state The message has been rejected by a delivery interface");
        errorCodes.put("109", "Message discarded");
        errorCodes.put("111", "Receiver’s operator is not supported. Message will not be billed");
        errorCodes.put("112", "Alphaname (sender’s name) was not approved by operator");
        errorCodes.put("113", "Alphaname (sender’s name) was not approved by operator. Money for this SMS was returned");
        errorCodes.put("200", "Unknown error");
        errorCodes.put("201", "Wrong message ID");
        errorCodes.put("202", "Wrong sender ID or auth error");
        errorCodes.put("203", "Wrong recipient number");
        errorCodes.put("204", "Message too long or empty");
        errorCodes.put("206", "Billing error");
        errorCodes.put("207", "Too mach messages");
        errorCodes.put("208", "Message with id is already exists");
        errorCodes.put("211", "Error deleting sms");
        errorCodes.put("205", "Account disabled");
        errorCodes.put("209", "Your IP is disabled to send");
        errorCodes.put("210", "Your IP is not allowed to send");
        errorCodes.put("212", "Low connection speed");
    }

    private String generateXmlForSMS(SMSMessage message) {
        return
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                        "<package key=\"" + messageProvider.getSmsProviderKey() + "\">" +
                        "<message>" +
                        "<msg recipient=\"" + message.getAddress() + "\" sender=\"" + messageProvider.getSmsProviderSender() + "\" type=\"0\">" + message.getBody() + "</msg>" +
                        "</message>" +
                        "</package>";

    }

    private String generateXmlForReport(SMSMessage message) {
        return
                "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                        "<package key=\"" + messageProvider.getSmsProviderKey() + "\">" +
                        "<status>" +
                        "<msg sms_id=\"" + message.getSmsId() + "\"></msg>" +
                        "</status>" +
                        "</package>";

    }

}
