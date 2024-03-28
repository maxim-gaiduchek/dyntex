import '@mantine/core/styles.css';
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
    element: <MainPage/>,
  },
  {
    path: "/secondary",
    element: <SecondaryPage/>
  },
  {
    path: "*",
    element: <NotFound/>
  },
  {
    path: "/account",
    element: <Account/>
  }
]);

function App() {
  return (
    <RouterProvider router={router} />  
  );
}

export default App;
