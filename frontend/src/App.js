import '@mantine/core/styles.css';
import MainPage from './Components/Pages/MainPage';
import SecondaryPage from './Components/Pages/SecondaryPage';
import NotFound from './Components/Pages/NotFound';

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
  }
]);

function App() {
  return (
    <RouterProvider router={router} />  
  );
}

export default App;
