-- Create Database
CREATE DATABASE BranchingOut;

-- Use the Database
USE BranchingOut;

-- Create Roles Table
CREATE TABLE Roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

-- Insert a role for standard users
INSERT INTO Roles (role_name) VALUES ('ROLE_USER');

-- Create User Table
CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Username NVARCHAR(50),
    Password NVARCHAR(50),
    Email NVARCHAR(100),
    RoleID INT,
    FOREIGN KEY (RoleID) REFERENCES Roles(role_id)
	UserID CHAR(36) DEFAULT UUID() AFTER Email
);

-- Create Family Table
CREATE TABLE Family (
    FamilyID INT PRIMARY KEY,
    FamilyName VARCHAR(255) NOT NULL,
    UserID INT, -- New column for the foreign key
    FOREIGN KEY (UserID) REFERENCES User(UserID) -- Foreign key constraint
);

-- Create FamilyMember Table
CREATE TABLE FamilyMember (
    MemberID INT PRIMARY KEY,
    FirstName VARCHAR(255) NOT NULL,
    LastName VARCHAR(255) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Relationship VARCHAR(50) NOT NULL,
    Gender VARCHAR(10) NOT NULL,
    ProfilePicture VARCHAR(255),
    UserID INT,
    FamilyID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID),
    FOREIGN KEY (FamilyID) REFERENCES Family(FamilyID)
);

-- Create ContactDetails Table
CREATE TABLE ContactDetails (
    ContactID INT PRIMARY KEY,
    PhoneNumber VARCHAR(20),
    Address VARCHAR(255),
    Email VARCHAR(255),
    SocialMedia VARCHAR(255),
    MemberID INT,
    FOREIGN KEY (MemberID) REFERENCES FamilyMember(MemberID)
);

-- Create Event Table
CREATE TABLE Event (
    EventID INT PRIMARY KEY,
    EventName VARCHAR(255) NOT NULL,
    Date DATE NOT NULL,
    Time TIME NOT NULL,
    Location VARCHAR(255),
    Description TEXT,
    FamilyID INT,
	FOREIGN KEY (UserID) REFERENCES User(UserID),
    FOREIGN KEY (FamilyID) REFERENCES Family(FamilyID)
);

-- Create Photo Table
CREATE TABLE Photo (
    PhotoID INT PRIMARY KEY,
    Image LONGBLOB,
    Description TEXT,
    DateAdded DATE NOT NULL,
    MemberID INT,
    FOREIGN KEY (MemberID) REFERENCES FamilyMember(MemberID)
);
