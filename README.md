Bank-Account-Management-System
==============================

A simple bank account management system that lets bank customers view their account balance and transactions, 
and make deposits and electronic payments.

The site provides 2 interfaces. First, a Manager interface that can be used by the bank manager to create new user accounts. 
Second, a Customer interface that lets users consult their account balance and transactions. 
In addition, this interface should let customers deposit funds to their accounts, 
and make payments to pre-registered electronic payees.

Requirements
The application is implemented by using the following technologies: Java Struts, JDBC, XHTML, CSS, JavaScript, and DOM.
It Stores all persistent information (deposits, payments, etc) on a MySql database

- The application Store the database connection information (username, password, connection URL) as parameters in the web.xml file.

- The user table stores the customer's login, but not their password. 
  Instead, for every customer it stores a salt value and a hash of the user's password concatenated with the salt value. 
  This is a common technique used to prevent storing password in clear text, which could be easily stolen. 

- Users should login to the system before being able to see any account information or make any bank transactions.
  There is one privileged account (manager account) that can access the functionality provided by the manager interface. 
  This functionality should not be available to regular customers. 
  Differentiate between privileged and regular users (i.e., customers) using the admin attribute of the user table.

- Assume that every costumer has only 1 bank account.

- To make a payment, the user selects a payee from the list of pre-loaded payees, and selects an amount. 
  Assume that all payments are due immediately (no future payments).

- Account deposits and payments make use of database transactions when appropriate to avoid inconsistencies in the event of failures.

- This application support concurrency control to ensure your application works properly in the presence of simultaneous access by multiple users.

- The Manager interface provide a way to list all customers, and for each customer view his/her current account balance and all transactions

The main entry page for your site should be called index.html.
