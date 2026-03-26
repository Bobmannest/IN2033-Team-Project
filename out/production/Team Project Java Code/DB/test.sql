CREATE DATABASE IF NOT EXISTS Test;
USE Test;

CREATE TABLE IF NOT EXISTS Invitation (
	Invitation_ID INT PRIMARY KEY,
	Host_ID INT,
	Date_of_Party DATE,
	Time_of_Party TIME,
	Description VARCHAR(250)
);

/* INSERT INTO Invitation (Invitation_ID, Host_ID, Date_of_Party, Time_of_Party, Description) VALUES
	(1, 1, '2025-03-15', '18:00:00', 'Alices birthday party with Blue Cake'),
	(2, 2, '2025-04-10', '14:00:00', 'Bobs BBQ Birthday'),
	(3, 3, '2025-05-22', '20:00:00', 'Charlies EPIC party!'),
	(4, 4, '2025-06-05', '16:30:00', 'Danas garden party with concert'),
	(5, 5, '2025-07-12', '19:00:00', 'Evas birthday with chocolate fountain'); */

SELECT * FROM Invitation