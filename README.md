swappr
======

Client / Server app for managing shift swaps

## Running the app

Swappr uses MySQL. Before running for the first time, ensure the database and user exist and have appropriate 
permissions. E.g. by executing the contents of [schema.sql](web/src/db/schema.sql).

Start the MySQL server:

```
mysql.server start
```

Then set up the db:

```
mysql -u root < schema.sql
```

and run the seed:

```
mysql -u root < seed.sql
```

## Smoke tests
### Requirements
Install [PhantomJS](http://phantomjs.org/download.html) eg. using Homebrew on Mac:

```
brew update && brew install phantomjs
```

Navigate to /integration/smoke_tests and run

```
bundle install
```

### Running
To run the tests locally, ie localhost:8080, use:

```
rake smoke_test
```

This will reset the database and seed it with 2 users, bill and ben, before running the test suite.

To run the test suite against another instance of the app, pass the hostname of that server as an argument:

```
rake "smoke_test['http://www.google.com']"
```

### Writing
The tests are written in [Capybara](https://github.com/jnicklas/capybara).

Shared code for steps is in step_definitions/common_steps.rb

### Debugging

To aid debugging of tests, open integration/smoke_tests/spec/spec_helper.rb and set

```
Capybara.default_driver = :selenium
```

This will run the tests in Firefox by default so you can see what's going on.

To set a breakpoint in a test, insert this line into your step definition:

```
binding.pry
```

This will pause execution in the command prompt and let you examine variables, run Capybara queries etc. You can also step through the test using next / continue. See [pry-nav](https://github.com/nixme/pry-nav)