
INSERT INTO "VEHICLE"
(ID, BRAND, COLOR, CREATE_DATE, LICENCE_PLATE, MODEL, "TYPE", UPDATE_DATE)
VALUES ('2c9666e9-637c-4c89-883d-4197d0326d44', 'AUDI', 'GREEN', '2024-09-12 07:09:04', 'OIE0244', 'A3', 'CAR','2024-09-12 07:09:04');

INSERT INTO "SPOT"
(ID, CITY, COMPLEMENT, COUNTRY, "NUMBER", STATE, STREET, ZIP_CODE, CAR_PARKING_SPACES, CNPJ, CREATE_DATE, MOTORCYCLE_PARKING_SPACES, NAME, PHONE_NUMBER, UPDATE_DATE)
VALUES('56d03dc0-06c6-41f0-a07d-05b7b2bf6383', 'FORTALEZA', '', 'BRAZIL', '876', 'CE', 'JOSE BARBOSA', '60145672', 45, '97289363780001', '2024-09-13 07:09:04', 34, 'ESPAÇO CAR', '85927638966', '2024-09-13 07:09:04');

INSERT INTO "PARK"
(ID, CREATE_DATE, ENTRY_DATE, EXIT_DATE, UPDATE_DATE, SPOT_ID, VEHICLE_ID)
VALUES('d0c61506-1a86-48ff-a4bd-297f5d6a9d6e', '2024-09-13 07:09:04', '2024-09-14 07:09:04', NULL , '2024-09-13 07:09:04', '56d03dc0-06c6-41f0-a07d-05b7b2bf6383', '2c9666e9-637c-4c89-883d-4197d0326d44');