import '@mantine/core/styles.css';
import MainLayout from './Components/Layout/MainLayout';
import MainPage from './Components/Pages/MainPage';
import SecondaryPage from './Components/Pages/SecondaryPage';
import NotFound from './Components/Pages/NotFound';
import Account from './Components/Pages/Account';
import LoginPage from './Components/Login/LoginPage';
import MediaPage from './Components/Pages/MediaPage';
import RegistrationPage from './Components/Login/RegistrationPage';
import ResetPage from './Components/Login/ResetPage';
import EditorPage from './Components/Pages/EditorPage'
import { useCookies } from 'react-cookie';
import axios from 'axios';
import { useEffect } from 'react';
import '@mantine/dropzone/styles.css';
import BaseUrl from './BaseUrl';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import ServerDown from './Components/Pages/ServerDown';
import Dashboard from './Components/Pages/Dashboard';
import TexturePage from './Components/Textures/TexturePage';
import Users from './Components/Pages/Users';
import Favourites from './Components/Pages/Favourites';
import Tags from './Components/Pages/Tags';
import MaskPage from './Components/Textures/MaskPage';
import DownloadPage from './Components/Pages/DownloadPage';
import Unauthorized from './Components/Pages/Unathorized';
import Verified from './Components/Pages/Verified';
import { getUser } from './utils';

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout><Dashboard/></MainLayout>,
  },
  {
    path: "/textures",
    element: <MainLayout><TexturePage/></MainLayout>
  },
  {
    path: "/users",
    element: <MainLayout><Users/></MainLayout>
  },
  {
    path: "/favourites",
    element: <MainLayout><Favourites/></MainLayout>
  },
  {
    path: "/secondary",
    element: <MainLayout><SecondaryPage/></MainLayout>
  },
  {
    path: "/tags",
    element: <MainLayout><Tags/></MainLayout>
  },
  {
    path: "/masks",
    element: <MainLayout><MaskPage/></MainLayout>
  },
  {
    path: "/account/:id",
    element: <MainLayout><Account/></MainLayout>
  },
  {
    path: "/media",
    element: <MainLayout><MainPage/></MainLayout>
  },
  {
    path: "/media/:id",
    element: <MainLayout><MediaPage type={"video"}/></MainLayout>
  },
  {
    path: "/mask/:id",
    element: <MainLayout><MediaPage type={"mask"}/></MainLayout>
  },
  {
    path: "/login",
    element: <LoginPage/>
  },
  {
    path: "/register",
    element: <RegistrationPage/>
  },
  {
    path: "/reset",
    element: <ResetPage/>
  },
  {
    path: "/editor/:id",
    element: <EditorPage/>
  },
  {
    path: "/serverdown",
    element: <ServerDown/>
  },
  {
    path: "*",
    element: <NotFound/>
  },
  {
    path: "/download/:id",
    element: <DownloadPage/>
  },
  {
    path: "/unauthorized",
    element: <Unauthorized/>
  },
  {
    path: "/verify",
    element: <Verified/>
  }
]);

function App() {
  const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
  const allowed = ["/login", "/register", "/serverdown", "/editor", "/download", "/verify"]
  const options = {
    headers: {
      'Authorization': cookies.token
    }
  };

  const checkLogged = async () => {
    if(allowed.includes(window.location.pathname)){
      return
    }
    if(cookies.token === undefined){
      // navigate("/login")
      window.location.replace("/login")

    }
    console.log(window.location.pathname)
    if(cookies.token !== undefined){
      try {
          const response = await axios.get(BaseUrl + "/api/users/authenticated", {
              headers: {
                  'Authorization': cookies.token
              }
          });
          console.log("dsauiDHIUAHDUIASU")
          if(window.location.pathname === "/unauthorized"){
            window.location.replace("/")
          }
          // return response.data;
      } catch (e) {
          if(e.code === "ERR_NETWORK"){
            window.location.replace("/serverdown")
          }
          if(e.response.status === 403 && window.location.pathname !== "/unauthorized"){
            window.location.replace("/unauthorized")
          }
          // console.error(e);
          // removeCookie("token");
          // navigate("/login");
      }
    }

    // this code fucking sucks for some reason and breaks login (idk probably something about how react states work).
    // the issue should resolve itself when we move to different tokens (waiting for backend) =).

    // try{
    //   const response = await axios.get("http://localhost:8080/api/users/authenticated", options)
    //   // setUser(response.data)
    // }catch(e){
    //   //very very bad and stupid =)
    //   console.log(e)
    //   removeCookie("token")
    //   // navigate("/login")
    //   window.location.replace("/login")
    // }

  }

  useEffect(() => {
    const script = document.createElement('script');
    
    script.src = "https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.1.1/crypto-js.min.js";
    script.async = true;
  
    document.body.appendChild(script);
    // checkLogged()
  },[])

  useEffect(() => {
    checkLogged()
  },[cookies.token])

  return (
    <RouterProvider router={router} />  
  );
}

export default App;
