# warehouse-be
Warehouse Management Microservices

## Introduction

This repository contains the first implementation of a back end warehouse for a Solution platform with the following components :

* Angular SPA ([Warehouse-fe](https://github.com/djomoutresor1/wharehouse-fe))
* Spring Web Application (Solution Web)

## Repository Structure

For specific folder's src/main/java package contains the followings sections:

~~~c
├── configuration/
├── constants/
├── controller/
├── exception/
├── filter/
├── model/
├── payload/
├── repositrory/
├── services/
├── utils/
├── warehouseAdvice
└── WarehouseBeApplication.java
~~~

* configuration: contains the java classes configuration.
* constants: contains the specifics constants description used in all the application.
* controller: contains the java classes endpoints into the application, the specific request call with the client.
* exception: contains the implementation of all type of exceptions handler.
* model: contains the dto of entities.
* payload: contains the request and response classes call to the endpoints. 
* repository: contains interfaces contract of CRUD operations.
* sercices: represents all the implementations of business logic call.
* utils: contains the commons utils call implementation.
* warehouseAdvice: ....
* WarehouseBeApplication.java: the entry point of the application.

##Test in your local environment
For set up the environement localy, follow the steps belongs.

**Prerequisites**:
* Docker

From the folder containing the file docker-compose.yml and use the following command:
```
docker-compose up
```
After that we have to edit the secret in secret.json file with the correct consumer key and consumer secret and we put it in locastack secretmanager with:

```
aws --endpoint-url=http://localhost:4566 secretsmanager put-secret-value --secret-id glin_ap31312mp00016_${ENV}_secret_ap35602gasmartmaint --secret-string file://secret.json
```

In the end we have to create the role for read the secret using the policy.json file:
```
aws --endpoint-url=http://localhost:4566 --region eu-central-1 iam create-role --role-name dummy --assume-role-policy-document file://policy.json
```



