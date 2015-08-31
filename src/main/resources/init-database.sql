CREATE table if not exists user_accounts(
id identity,
email VARCHAR(100) NOT NULL,
first_name VARCHAR(100) NOT NULL,
last_name VARCHAR(100) NOT NULL,
password VARCHAR(255)NOT NULL,
role VARCHAR(20) NOT NULL);


CREATE table if not EXISTS file_metadatas(
id identity,
file_type VARCHAR(100),
file_size BIGINT
);

CREATE table if not exists files(id identity,
file_name VARCHAR(100),
  upload_note VARCHAR(255),
file_metadata_id BIGINT,
FOREIGN KEY(file_metadata_id) REFERENCES file_metadatas(id));


CREATE table if not exists user_files(file_id BIGINT,user_id BIGINT,
PRIMARY KEY(file_id),
FOREIGN KEY (user_id) REFERENCES user_accounts(id),
FOREIGN KEY(file_id) REFERENCES files(id));

INSERT INTO user_accounts(email,first_name,last_name,password,role) VALUES ('kmsadmin@kms-technology.com','Admin','The Great','iamthekingofthiswebsite','ADMIN');