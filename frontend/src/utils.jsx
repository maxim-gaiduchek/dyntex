import axios from 'axios';
import BaseUrl from './BaseUrl';

function updateToken(newToken) {
    const cookies = document.cookie.split(';');
    
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        
        if (cookie.startsWith('token=')) {
            document.cookie = `token=${newToken}; path=/;`;
            return;
        }
    }
    
    var expires = new Date();
    expires.setFullYear(expires.getFullYear() + 1);
    document.cookie = `token=${newToken};`;
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
        },
        withCredentials: true
      };
    const response = await axios.get(BaseUrl + "/api/security/refresh", options);
    
    updateToken(response.data.accessToken);
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
            //try again =)
            try{
                const response2 = await axios[method](BaseUrl + path, data, options);
                return {"status": response2.status, "data": response2.data};
            }catch(e){
                return {"status": e, "data": e};
            }
        }
        return {"status": e, "data": e};
    }

}