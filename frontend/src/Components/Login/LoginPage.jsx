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


  export default function LoginPage() {
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [error, setError] = React.useState([false, false])

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

    const hashPasswd = async () => {
      const encoder = new TextEncoder();
      const data = encoder.encode(password);
      const hashBuffer = await crypto.subtle.digest('SHA-256', data);
      const hashArray = Array.from(new Uint8Array(hashBuffer));
      return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
    }

    const login = async () => {
        if(!error[0] && !error[1]){
            try {
              const response = await axios.post('http://localhost:8080/api/users/login', {
                email,
                "password": await hashPasswd(),
              });
              
              notifications.show({
                  title: 'Logged in',
                  color: 'green',
                  icon: <IconCheck/>,
                  autoClose: 4000,
                  message: 'User logged in ' + response.data.name,
              })
            } catch (error) {
              console.log(error)
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
            <Checkbox label="Remember me" />
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