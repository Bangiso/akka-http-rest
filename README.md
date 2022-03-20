# akka-http-rest
Simple api with mysql db, exposing the following endpoints. See `requests.sh`.

- `
POST /api/students
` expectin the payload `{"id":1,"name":"Leo","gpa":70}`

- `
GET /api/students
`

- `
GET /api/students/{id}
`

- `
DELETE /api/students/{id}
`

# Database

Requires db connection table outlined in `db-setup.sql` file. The credentials can be modified in the application.conf file.

