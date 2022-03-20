function getData() {
  const apiUrl = 'http://localhost:8080/app/meetings';
  return fetch(apiUrl)
    .then((response) => response.json())
    .then((data) => {
      return data;
    });
}
export default getData;