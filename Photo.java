package models;

import java.sql.Date;

public class Photo {
	private int photoID;

    private byte[] image;
    private String description;
    private Date dateAdded;
    private FamilyMember familyMember;
}
