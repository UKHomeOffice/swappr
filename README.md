swappr
======

Client / Server app for managing shift swaps

running
=======

Swappr uses MySQL. Before running for the first time, ensure the database and user exist and have appropriate 
permissions. E.g. by executing the contents of [schema.sql](web/src/db/schema.sql).

```
mysql -u root < schema.sql
```

then run the seed:
```
mysql -u root < seed.sql
```