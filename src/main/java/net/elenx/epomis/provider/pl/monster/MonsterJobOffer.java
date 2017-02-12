package net.elenx.epomis.provider.pl.monster;


import lombok.Data;

@Data
class MonsterJobOffer {
    private String url;
    private String title;
    private HiringOrganization hiringOrganization;
    private JobLocation jobLocation;
}

@Data
class HiringOrganization {
    private String name;
}

@Data
class JobLocation {
    private Address address;
}

@Data
class Address {
    private String addressLocality;
}






