import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final String CURRENT_DIRECTORY = System.getProperty("user.dir");

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        File folder = new File(CURRENT_DIRECTORY);
        System.out.println(CURRENT_DIRECTORY);
        File[] listOfFiles = folder.listFiles();

        File export = new File("wifi_password.txt");
        FileWriter myWriter = new FileWriter("wifi_password.txt");

        for (File file : listOfFiles) {

            if (file.isFile()) {
                try {
                    System.out.println(file.getName());
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(new File(CURRENT_DIRECTORY + "\\" + file.getName()));
                    doc.getDocumentElement().normalize();
                    Element WLANProfile = doc.getDocumentElement();
                    String SSID = getValue("name", WLANProfile);

                    Element MSM = (Element) WLANProfile.getElementsByTagName("MSM").item(0);
                    Element security = (Element) MSM.getElementsByTagName("security").item(0);

                    Element sharedKey = (Element) security.getElementsByTagName("sharedKey").item(0);
                    String password = getValue("keyMaterial", sharedKey);
                    System.out.println(password);

                    myWriter.write("SSID: " + SSID + "\nPass: " + password + "\n\n");
                } catch (Exception ex) {
                    continue;
                }
                System.out.println("\n");
            }
        }

        myWriter.close();
    }

    public static String getValue(String tag, Element element) {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }
}
