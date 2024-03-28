import '@mantine/core/styles.css';
import MainLayout from './Components/Layout/MainLayout';
import MainPage from './Components/Pages/MainPage';
import SecondaryPage from './Components/Pages/SecondaryPage';
import NotFound from './Components/Pages/NotFound';
import Account from './Components/Pages/Account';

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
