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
    
    document.cookie = `token=${newToken};`;
}

export const getUser = async (token) => {

    //write old getUser
    // const options = {
    //     headers: {
    //       'Content-Type': 'multipart/form-data',
    //      'Authorization': "Bearer " + token
    //     },
    //   };
    // const response = await axios.get(BaseUrl + "/api/users/authenticated", options);
    // return response.data;
    return await callApi("/api/users/authenticated", "get", undefined, token);
};

const refreshToken = async (token) => {
    //check if e.code == 403 or 500
    try{
        const options = {
            headers: {
            },
            withCredentials: true
          };
        const response = await axios.get(BaseUrl + "/api/security/refresh", options);
        
        updateToken(response.data.accessToken);
    }catch(e){
        console.log(e)
        //refresh token has expired
        // document.cookie = ""
        // window.location.href = "/login"
    }
}
export const callApi = async (path, method, data = undefined, token = undefined, options = undefined) => {
    if(options === undefined){
        if(token !== undefined){
            options = {
                headers: {
                  'Content-Type': 'multipart/form-data',
                  'Authorization': "Bearer " + token
                },
                withCredentials: true
              };
        }else{
            options = {
                headers: {
                  'Content-Type': 'multipart/form-data',
                },
                withCredentials: true
              };
        }
    }
    //TODO: handle access token refresh
    try{
        var response
        if(data === undefined){
            response = await axios[method](BaseUrl + path, options = options);
        }else{
            response = await axios[method](BaseUrl + path, data, options = options);
        }
        return {"status": response.status, "data": response.data};
    }   catch (e) {
        console.log("PENIS PENIS", e)
        if(token !== undefined){
            // refreshToken(token);
            //try again =)
            // try{
            //     const response2 = await axios[method](BaseUrl + path, options = options, data = data);
            //     return {"status": response2.status, "data": response2.data};
            // }catch(e){
            //     return {"status": e, "data": e};
            // }
        }
        return {"status": e, "data": e};
    }

}