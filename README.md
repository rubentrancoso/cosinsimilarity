# cosinsimilarity

The solution is a self-contained spring boot application that provides a simple rest service to find the top 10 similar items. 

localhost:8080/recommend/45 -> will return 

```
{
   result: [
         {
            id: 1321506,
            sku: 67,
            similarity: 0.9360594897015758,
            similarSku: 3784
         },
         
         ... some more
         
         {
            id: 503433,
            sku: 26,
            similarity: 0.9139760899701568,
            similarSku: 3784
         }
   ],
   status: success"
}
```

You will need an MYSQL instance with a schema and a user/password with the privileges on it.
spring.datasource.url = jdbc:mysql://localhost:3306/myschema
spring.datasource.username = user
spring.datasource.password = 123456

## Running

To run it, use java -jar re-0.1.0.jar

within the same folder as the application, you will find the config/application.properties file.

**The parameters I've prepared are:**

hibernate.jdbc.batch_size = 200000
   the ammount of records to be inserted on the database within a bulk insert

initializer.file.output=/tmp/output.csv
   an intermediate file with data generated with cosin similarity

initializer.file.data=data.json
   the json file to be loaded: data.jon or shordata.json that contains only 100 records.

initializer.lambda=false
   a test made with a lambda function to load data from output.csv into the database

initializer.skip=true
   to use a already set database and jump over the initialization ( remember to change spring.jpa.hibernate.ddl-auto to update so you do not get the schema totally droped )

dotproduct.weighted=true
   give you the option to calculate cosin similarity with or without the weights

\# a(10) to j(1)
dotproduct.weigths=10,9,8,7,6,5,4,3,2,1
   the weights from a to j


## Notes
in a modest mac mini it takes 3 minutes to generate all 1/2 * m (m-1) comparisons and 15 hours to load all the 199,990,000 similarities.

The last change I did, was to add the index I forgot,  on the similarity entity. So depends on the hardware it may not run very well.
I did a try for two in memory libs on my way to this outcome, and I could also tell you that mongo or prevayler should be some options, but time and loneliness do not help too much in this case. All must be discussed according to the needs.

 So, that is a bit of the discussion that this solution will raise and I will be happy to have this conversation with you all.
