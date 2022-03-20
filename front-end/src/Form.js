
import Autocomplete from "react-google-autocomplete";
import { useState, useEffect } from "react";
import getData from './getData'

function Form(props) {
    const [name, setName] = useState("");
    const [date, setDate] = useState("");
    const [location, setLocation] = useState("");
    const [message, setMessage] = useState("");
    const [refInput, setRefInput] = useState("");
  
    let handleSubmit = async (e) => {
      e.preventDefault();
      try {
        let res = await fetch("http://localhost:8080/app/meetings", {
          method: "POST",
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            person: name,
            date: date,
            location: location,
          }),
        });
        let resJson = await res.json();
        if (res.status === 200) {
          setName("");
          setDate("");
          setLocation("");
          refInput.value="";
         // setMessage("Meeting created successfully");
          getData().then(data => {
            props.setData(data);
          });
        } else {
          setMessage("Error: " + resJson.message);
        }

      } catch (err) {
        console.log(err);
      }
    };
  
    return (
      <div className="Form">
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            value={name}
            placeholder="Name"
            onChange={(e) => setName(e.target.value)}
          />
          <input
            type="Date"
            value={date}
            placeholder="Date"
            onChange={(e) => setDate(e.target.value)}
          />
          <Autocomplete
            apiKey="AIzaSyDmWLyZux2H6q_ktgqEs23UPz06lj5ItFQ"
            onPlaceSelected={(place, inputRef, autocomplete) => {
            setLocation(place.formatted_address);
            setRefInput(inputRef);

           }}
           options={{
            types: ["geocode"],
          }}
          />
          <button type="submit">Create</button>
          <div className="message">{message ? <p>{message}</p> : null}</div>
        </form>
      </div>
    );
  }

  export default Form;