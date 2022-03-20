echo -e "\n==================================="
echo "POST Request"
curl -X POST -H "Content-Type: application/json"  -d '{"id":5,"name":"Leo","gpa":70}' http://localhost:8088/api/students
echo -e "\n==================================="
echo "GET Request with path params"
curl http://localhost:8088/api/students/5
echo -e "\n==================================="
echo -e "GET Request"
curl http://localhost:8088/api/students
echo -e "\n==================================="
echo -e "DELETE Request"
curl -X DELETE  http://localhost:8088/api/students/5
echo -e "\n==================================="
echo -e "FINISHED.........."
echo -e "==================================="



