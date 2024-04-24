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
import '@mantine/dropzone/styles.css';

import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout><MainPage/></MainLayout>,
  },
  {
    path: "/secondary",
    element: <MainLayout><SecondaryPage/></MainLayout>
  },
  {
    path: "/account",
    element: <MainLayout><Account/></MainLayout>
  },
  {
    path: "/account/:id",
    element: <MainLayout><Account/></MainLayout>
  },
  {
    path: "/media/:id",
    element: <MainLayout><MediaPage/></MainLayout>
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
    path: "/editor",
    element: <EditorPage/>
  },
  {
    path: "*",
    element: <NotFound/>
  }
]);

function App() {
  return (
    <RouterProvider router={router} />  
  );
}

export default App;
