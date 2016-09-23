# _Hair Salon_

#### By _Ewa Manek, 9/23/2016_

## Description

_A Hair Salon App with pages for employee and client management, built using Java, Spark and Postgres_

## Setup/Installation Requirements

* _Copy the repository from GitHub_
* _Make sure you have gradle and postgresql installed!_
* _Use the following commands in psql to create the postgres database or just create the hair_salon database and run 'psql hair_salon < hair\_salon.sql'_
 * CREATE DATABASE hair_salon
 * CREATE TABLE stylists (id serial PRIMARY KEY, name varchar, specialty varchar, accepting_clients boolean, experience int, hours varchar);
 * CREATE TABLE clients (id serial PRIMARY KEY, name varchar, stylist_id int);
* _gradle will download and install junit and spark_
* _the 'gradle run' command will deploy the site to port 4567 by default_

## Technologies used

* Java 1.8.0_101
* Gradle 3.0
* JUnit 4.+
* Spark 2.3
* Velocity Template Engine 1.7
* Postgresql 9.6

## GitHub link

https://github.com/ewajm/salonJava

## Licensing

* MIT

Copyright (c) 2016 **_Ewa Manek_**
