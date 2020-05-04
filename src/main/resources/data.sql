DROP TABLE IF EXISTS users;
 
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  id_proof VARCHAR(20) NOT NULL,
  id_type VARCHAR(50) NOT NULL,
  mobile BIGINT(15) NOT NULL
);
 
INSERT INTO users (username, id_proof, id_type, mobile) VALUES
  ('Rishi Kapoor','DLFAP929292OOO9292', 'Driving License', 9876543210),
  ('Irfan Khan','AKIJD847UJ', 'PAN', 9876543211),
  ('Vishal Kumar','8774983984985895', 'Aadhar', 9876543212);