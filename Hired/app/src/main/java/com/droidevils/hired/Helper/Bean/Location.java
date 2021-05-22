package com.droidevils.hired.Helper.Bean;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Location {

    private double latitude;
    private double longitude;
    private String address;

    public Location() {
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location getLocation(String addressString) {
        Location location = new Location();
        location.setAddress(addressString);
        try {
            location.getLocation();
            return location;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getLocation() throws IOException, ParserConfigurationException, SAXException {
        String baseURL = "https://nominatim.openstreetmap.org/search.php?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
        String finalURL = baseURL + "&format=xml";
        URL url = new URL(finalURL);
        URLConnection conn = url.openConnection();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(conn.getInputStream());
        NodeList layerConfigList = doc.getElementsByTagName("place");
        Node node = layerConfigList.item(0);
        Element e = (Element) node;
        latitude = Double.parseDouble(e.getAttribute("lat"));
        longitude = Double.parseDouble(e.getAttribute("lon"));
    }

    public double computeDistance(Location location) {
        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(location.getLatitude() - latitude);
        double dLon = Math.toRadians(location.getLongitude() - longitude);
        double a;
        a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(location.getLatitude())) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = r * c;
        return Math.round(distance * 100 / 100);
    }

    public static double computeDistance(Location location1, Location location2) {
        return location1.computeDistance(location2);
    }

    public void displayLocation() {
        System.out.println("Lat' " + latitude + " : " + "Long' " + longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
