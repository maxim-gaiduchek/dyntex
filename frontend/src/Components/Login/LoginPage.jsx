import {
    TextInput,
    PasswordInput,
    Checkbox,
    Anchor,
    Paper,
    Title,
    Text,
    Container,
    Group,
    Button,
  } from '@mantine/core';
import { IconExclamationCircle, IconCheck } from '@tabler/icons-react';
import classes from './LoginPage.module.css';
import { Link } from 'react-router-dom';
import React from 'react';
import { notifications } from '@mantine/notifications';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import BaseUrl from '../../BaseUrl';

  export default function LoginPage() {
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [remember, setRemember] = React.useState(false);
    const [error, setError] = React.useState([false, false])
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
    const navigate = useNavigate()

    const checkEmail = (event) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        let text = event.target.value;

        let errcpy = [...error];
        
        errcpy[0] = text === "" ? false : !emailRegex.test(text);
        setError(errcpy);
        setEmail(text);
    }

    const checkPassword = (event) => {
        setPassword(event.target.value)
    }

    const hashPasswd = async (password) => {
      const response = await fetch('https://api.hashify.net/hash/sha256/hex', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ data: password })
      });
      
      if (!response.ok) {
        throw new Error('Failed to hash password');
      }
    
      const { Digest } = await response.json();
      return Digest;
    }

    useEffect(() => {
      //TODO: probably should check authorized status when email releases =)
      if(cookies.token !== undefined){
        navigate("/")
      }
    },[])

    const login = async () => {
        if(!error[0] && !error[1]){
            try {
              const response = await axios.post(BaseUrl+'/api/security/login', {
                "login": email,
                "password": await hashPasswd(),
              });
              
              notifications.show({
                  title: 'Logged in',
                  color: 'green',
                  icon: <IconCheck/>,
                  autoClose: 4000,
                  message: 'User logged in',
              })
              const expirationDate = new Date();
              expirationDate.setDate(expirationDate.getDate() + 30);
              console.log(expirationDate)
              if(remember){
                setCookie("token", response.data.accessToken, { expires: expirationDate })
                setCookie("id", response.data.userId, { expires: expirationDate })
              }else{
                setCookie("token", response.data.accessToken)
                setCookie("id", response.data.userId)
              }
              navigate("/")
            } catch (error) {
              console.log(error)
              if(error.code === "ERR_NETWORK"){
                navigate("/serverdown")
                return
              }
              notifications.show({
                  title: 'Server error',
                  color: 'red',
                  icon: <IconExclamationCircle/>,
                  autoClose: 4000,
                  message: error.response.data.description
              })
            }
            return;
        }

    }

    return (
      <Container size={420} my={40}>
        <Title ta="center" className={classes.title}>
          Welcome back!
        </Title>
        <Text c="dimmed" size="sm" ta="center" mt={5}>
          Do not have an account yet?{' '}
          <Anchor size="sm" component="button">
            <Link to="/register">Create account</Link>
          </Anchor>
        </Text>
  
        <Paper withBorder shadow="md" p={30} mt={30} radius="md">
          <TextInput label="Email" error={error[0]} value={email} onChange={checkEmail} placeholder="you@example.com" required />
          <PasswordInput label="Password" error={error[1]} value={password} onChange={checkPassword} placeholder="Your password" required mt="md" />
          <Group justify="space-between" mt="lg">
            <Checkbox label="Remember me" value={remember} onChange={(e) => setRemember(e.target.value)}/>
            <Anchor component="button" size="sm">
              <Link to="/reset">Forgot password?</Link>
            </Anchor>
          </Group>
          <Button onClick={login} disabled={(email === "" || password === "") ? true : error[0] || error[1]} fullWidth mt="xl">
            Sign in
          </Button>
        </Paper>
      </Container>
    );
  }