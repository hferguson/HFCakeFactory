drop TABLE if exists catalog;
create TABLE catalog (
	sku	varchar(20)		NOT NULL PRIMARY KEY,
	title varchar(255)	NOT NULL,
	price decimal		NOT NULL,
	description varchar(4000) NULL,
	image varchar(255)	NOT NULL DEFAULT 'http://placehold.it/700x400'	
);