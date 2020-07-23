create table APP_ROLE(
	ROLE_ID BIGINT not null,
    ROLE_NAME VARCHAR(30) not null
);
ALTER TABLE APP_ROLE ADD CONSTRAINT APP_ROLE_PK primary key (ROLE_ID);
ALTER TABLE APP_ROLE ADD CONSTRAINT APP_ROLE_UK unique (ROLE_NAME);

create table APP_USER(
	USER_ID BIGINT not null,
    USER_NAME nvarchar(100) not null,
    ENCRYTED_PASSWORD varchar(100) not null,
    ENABLED BIT not null,
    FIRST_NAME nvarchar(15),
    LAST_NAME nvarchar(15),
    STREET nvarchar(50),
    TOWN nvarchar(50),
    EMAIL varchar(50),
    PHONE varchar(15),
    RESET_TOKEN varchar(36)
);

ALTER TABLE APP_USER ADD CONSTRAINT APP_USER_PK primary key (USER_ID);
ALTER TABLE APP_USER ADD CONSTRAINT APP_USER_UK unique (USER_NAME);

create table USER_ROLE(
	ID BIGINT not null,
    USER_ID BIGINT not null,
    ROLE_ID BIGINT not null
);
alter table USER_ROLE add constraint USER_ROLE_PK primary key (ID);
alter table USER_ROLE add constraint USER_ROLE_UK unique (USER_ID, ROLE_ID);

alter table USER_ROLE add constraint USER_ROLE_FK1 foreign key (USER_ID)
references APP_USER (USER_ID);

alter table USER_ROLE add constraint USER_ROLE_FK2 foreign key (ROLE_ID)
references APP_ROLE (ROLE_ID);

insert into App_User (USER_ID, USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (1, 'quan', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
insert into APP_USER (USER_ID,USER_NAME, ENCRYTED_PASSWORD, ENABLED)
values (2, 'lan', '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);
---
 
insert into app_role (ROLE_ID, ROLE_NAME)
values (1, 'ROLE_ADMIN');
 
insert into app_role (ROLE_ID, ROLE_NAME)
values (2, 'ROLE_USER');

insert into app_role (ROLE_ID, ROLE_NAME)
values (3, 'ROLE_EMPLOYEE'); 
---
 
insert into user_role (ID, USER_ID, ROLE_ID)
values (1, 1, 1);
 
insert into user_role (ID, USER_ID, ROLE_ID)
values (2, 1, 2);
 
insert into user_role (ID, USER_ID, ROLE_ID)
values (3, 2, 2);

use clothes
create table PRODUCTS(
	CODE VARCHAR(20) not null,
    IMAGE longblob,
    NAME NVARCHAR(100) not null,
    DESCRIPTION NVARCHAR(100) not null,
    PRICE double precision not null,
    CREATE_DATE datetime not null
);

alter table PRODUCTS add constraint PRODUCT_PK primary key (CODE);
alter table PRODUCTS add column categories_id varchar(30);
alter table PRODUCTS
add constraint PRODUCT_CATE_FK foreign key (categories_id)
references categories (ID);

create table ORDERS(
  ID               VARCHAR(50) not null,
  AMOUNT           double precision not null,
  CUSTOMER_ADDRESS VARCHAR(255) not null,
  CUSTOMER_EMAIL   VARCHAR(128) not null,
  CUSTOMER_NAME    VARCHAR(255) not null,
  CUSTOMER_PHONE   VARCHAR(128) not null,
  ORDER_DATE       datetime not null,
  ORDER_NUM        INTEGER not null
);

alter table ORDERS
  add constraint ORDERS_PK primary key (ID) ;
alter table ORDERS
  add constraint ORDER_UK unique (ORDER_NUM) ;
alter table orders add column status nvarchar(15);
-- Create table
create table ORDER_DETAILS
(
  ID         VARCHAR(50) not null,
  AMOUNT     double precision not null,
  PRICE      double precision not null,
  QUANITY    INTEGER not null,
  ORDER_ID   VARCHAR(50) not null,
  PRODUCT_ID VARCHAR(20) not null
) ;

alter table ORDER_DETAILS
  add constraint ORDER_DETAILS_PK primary key (ID) ;
alter table ORDER_DETAILS
  add constraint ORDER_DETAIL_FK1 foreign key (ORDER_ID)
  references ORDERS (ID);
alter table ORDER_DETAILS
  add constraint ORDER_DETAIL_FK2 foreign key (PRODUCT_ID)
  references PRODUCTS (CODE);
  
insert into products (CODE, NAME,DESCRIPTION, PRICE, CREATE_DATE)
values ('S001', 'Core Java','mo ta', 100, sysdate());
 
insert into products (CODE, NAME,DESCRIPTION, PRICE, CREATE_DATE)
values ('S002', 'Spring for Beginners','mo ta', 50, sysdate());
 
insert into products (CODE, NAME,DESCRIPTION, PRICE, CREATE_DATE)
values ('S003', 'Swift for Beginners','mo ta', 120, sysdate());
 
insert into products (CODE, NAME,DESCRIPTION, PRICE, CREATE_DATE)
values ('S004', 'Oracle XML Parser','mo ta', 120, sysdate());
 
insert into products (CODE, NAME,DESCRIPTION, PRICE, CREATE_DATE)
values ('S005', 'CSharp Tutorial for Beginers','mo ta', 110, sysdate());

create table categories(
	ID varchar(30),
    NAME varchar(50),
    Create_Date datetime
);
alter table categories add constraint categories_PK primary key (ID);
alter table categories add column category_parent_id varchar(30);
alter table categories
  add constraint cate_cateparent_FK foreign key (category_parent_id)
  references category_parent (ID);

create table category_parent(
	ID int auto_increment(30),
    NAME varchar(50),
    URL varchar(70)
);
alter table category_parent add constraint category_parent_PK primary key (ID);

create table carousel(
	id int primary key auto_increment,
    name nvarchar(30),
    IMAGE longblob
);
alter table carousel add column createdate datetime not null;
alter table carousel add column description nvarchar(250);
alter table carousel add column tag nvarchar(30);
alter table carousel add column url varchar(50);
alter table carousel add column title nvarchar(50);

create table contact(
	id int primary key auto_increment,
    address nvarchar(50) not null,
    phone nvarchar(15)  not null,
    email varchar(50)  not null
);