# B2BF
##### System dependencies
1. JDK11
1. elasticsearch 6.7.0
1. mysql 5.7
##### Library dependencies
1. spring boot 2.1.5
1. mysql jdbc
1. aws java sdk 1.11.556
1. elasticsearch 6.4.3
1. JPA
1. Maven
Maven will automatically install all the depencies through pom file.

##### Start the app
- `mysql -e 'create database bbf;'`
- change the username, password, aws access key, aws secret key and elastic search host/port in the application properties file.
- `./mvnw clean spring-boot:run`
the app will seed the database on startup with 2 products and index their data into elastic search.

 #####Design
The app has 2 tables, product and flag
 - it was required that the product to have a generated unique ID. java's UUID Class was used as the primary key for this table and JPA already provides a generator for this type of primary key.
 - according to the requirement, product had flags that could vary over time, could increase or decrease, for that i took the decision of creating a flag table that will hold the flags created in the system, whenever a product needs to be flagged by a flag it will be done through table product_flags which is a joint table between product and flag tables since it's a many-to-many relationship betweent the two tables.
 - when creating a new product, the user can pass the image as dataURI (data content type concatenated with base64 encoding of the image). in the API the image will be converted to a stream of bytes then passed to AWS SDK to uploaded as an object on S3 bucket with a randomly generated UUID as the name then the URL is saved in the product field imageUrl.

##### Using the app
- `./mvnw clean spring-boot:run`
- to list products `curl http://localhost:8080/products`
- to list a single product `curl http://localhost:8080/products/1-1-1-1-1` 1-1-1-1-1 is the UUID of the product
- to search for a product with keyword `curl http://localhost:8080/products?keyword=burger` it will match in title or description.
- to modify a product `curl -X PUT http://localhost:8080/products/1-1-1-1-1 -H 'Content-type:application/json' -d '{"price":5}'`
- to create a product `curl -X POST http://localhost:8080/products -H 'Content-type:application/json' -d '{"vendorUID":"vnd123","title":"nachos","description":"vegan burger nachos","price":1.5, imageUrl: IMAGE_AS_DATAURI}'`

