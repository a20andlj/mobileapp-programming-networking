package com.example.networking;


public class Mountain {
    private String id;
    private String name;
    private String type;
    private String company;
    private String location;
    private String category;
    private Integer size;
    private Integer cost;
    private Auxdata auxdata;

    public String getName() {return name;}

    public Auxdata getAuxdata() {return auxdata;}

    @Override
    public String toString() {return name + "  located at " + location;}
}
