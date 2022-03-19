import React, { useRef, useState } from 'react';

import Script from 'react-load-script';
//import SearchBar from 'material-ui-search-bar';

const Search = () => {
  const [query, setQuery] = useState('');
  const [city, setCity] = useState('');
  const [address, setAddress] = useState('');

  // Store autocomplete object in a ref.
  // This is done because refs do not trigger a re-render when changed.
  const autocompleteRef = useRef(null);

  const handleScriptLoad = () => {
    // Declare Options For Autocomplete
    const options = {
     // types: ['(cities)'],
    };

    // Initialize Google Autocomplete
    /*global google*/ // To disable any eslint 'google not defined' errors
    autocompleteRef.current = new google.maps.places.Autocomplete(
      document.getElementById('autocomplete'),
      options
    );

    // Avoid paying for data that you don't need by restricting the set of
    // place fields that are returned to just the address components and formatted
    // address.
    autocompleteRef.current.setFields(['address_components', 'formatted_address']);

    // Fire Event when a suggested name is selected
    autocompleteRef.current.addListener('place_changed', handlePlaceSelect);
  };

  const handlePlaceSelect = () => {
    // Extract City From Address Object
    const addressObject = autocompleteRef.current.getPlace();

    const address = addressObject.address_components;

    // Check if address is valid
    if (address) {
      setCity(address[0].long_name);
      setQuery(addressObject.formatted_address);
    }
  };

  return (
    <div>
      <Script
        url="https://maps.googleapis.com/maps/api/js?key=AIzaSyDmWLyZux2H6q_ktgqEs23UPz06lj5ItFQ&libraries=places"
       // url="https://maps.googleapis.com/maps/api/js?key=AIzaSyAHBRGcMi2EZk5mC91eQKn-hN5wppptMnQ&libraries=places&callback=initMap"
        onLoad={handleScriptLoad}
      />
      <input
        id="autocomplete"
        placeholder=""
        value={query}
        hintText="Search City"
        style={{
          margin: '0 auto',
          maxWidth: 800,
        }}
      />
    </div>
  );
};

export default Search;