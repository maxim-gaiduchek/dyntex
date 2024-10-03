import { Title, Text, Button, Container, Group } from '@mantine/core';
import classes from './NotFoundTitle.module.css';
import { Link } from 'react-router-dom';
import { useEffect } from 'react';
import axios from 'axios';
import BaseUrl from '../../BaseUrl';

export default function Verified() {
    const getToken = () => {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const authToken = urlParams.get('authToken');
        return authToken;
    }
    const sendToken = async () =>{
        try{
            const token = getToken();
            const response = await axios.get(BaseUrl + "/api/auth?authToken=" + token);
            console.log(response)
        }catch(e){
            console.log(e)
            // window.location.href = "/"
        }
    }
    //http://localhost:8080
    // function to get ?authToken= from the URL
    useEffect(()=>{
        sendToken()
    }, [])
  return (
    <Container className={classes.root}>
      <Title className={classes.title}>🎉Email verified🎉</Title>
      <Text c="dimmed" size="lg" ta="center" className={classes.description}>
        {/* write text for this page */}
        Thank you for verifying your email address. Your account is now fully activated, and you can enjoy all the features and services we offer.
      </Text>
      <Group justify="center">
        <Link to={"/"}>
            <Button>Home Page</Button>
        </Link>
      </Group>
    </Container>
  );
}