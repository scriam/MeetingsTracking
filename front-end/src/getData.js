export default function getData() {
    const apiUrl = 'http://localhost:8080/app/meetings';
   return fetch(apiUrl)
      .then((response) => response.json())
      .then((data) => {
        console.log('This is your data', data);
        return data;
      });
}