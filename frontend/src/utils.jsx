import axios from 'axios';
import BaseUrl from './BaseUrl';

function updateToken(newToken) {
    // Get all cookies
    const cookies = document.cookie.split(';');
    
    // Loop through cookies to find the token cookie
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        
        // If this is the token cookie, update it
        if (cookie.startsWith('token=')) {
            // Set the new token value, keeping other attributes (like path and expiration) the same
            document.cookie = `token=${newToken}; path=/; expires=Fri, 31 Dec 2024 23:59:59 GMT`;
            return;  // Exit once the token is updated
        }
    }
    
    // If token cookie doesn't exist, you can create a new one (optional)
    var expires = new Date();
    expires.setFullYear(expires.getFullYear() + 1);
    document.cookie = `token=${newToken}; path=/; ${expires}`;
}

export const getUser = async (token) => {
    //TODO: use callApi
    if (token === undefined) {
        return false
    }

    try {
        const response = await axios.get(BaseUrl + "/api/users/authenticated", {
            headers: {
                'Authorization': "Bearer " + token
            }
        });
        console.log(document.cookie)

        return response.data;
        //save new accessToken
    } catch (e) {
        return false
        // console.error(e);
        // removeCookie("token");
        // navigate("/login");
    }
};

const refreshToken = async (token) => {
    const options = {
        headers: {
            'Content-Type': 'multipart/form-data',

        },
        withCredentials: true
      };
    const response = await axios.get(BaseUrl + "/api/security/refresh", options);
    
    updateToken(response.data.accessToken);
    console.log(response.data)
}

export const callApi = async (path, method, data, token = undefined) => {
    var options;
    if(token !== undefined){
        options = {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Authorization': "Bearer " + token
            },
          };
    }else{
        options = {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          };
    }
    //TODO: handle access token refresh
    try{
        const response = await axios[method](BaseUrl + path, data, options);
        return {"status": response.status, "data": response.data};
    }   catch (e) {
        if(token !== undefined){
            refreshToken(token);
        }
        return {"status": e, "data": e};
    }

}