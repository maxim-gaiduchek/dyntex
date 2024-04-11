import {
    TextInput,
    PasswordInput,
    Anchor,
    Paper,
    Title,
    Text,
    Container,
    Group,
    Button,
    rem,
    Stepper
  } from '@mantine/core';
  import { IconMail, IconUser, IconKey } from '@tabler/icons-react';
  import classes from './LoginPage.module.css';
  import { Link } from 'react-router-dom';
  import React from 'react';

  export default function LoginPage() {

    const [active, setActive] = React.useState(0);
    const nextStep = () => setActive((current) => (current < 3 ? current + 1 : current));
    const prevStep = () => setActive((current) => (current > 0 ? current - 1 : current));

    const [email, setEmail] = React.useState("");

    const [name, setName] = React.useState("");
    const [surname, setSurname] = React.useState("");

    const [password, setPassword] = React.useState("");
    const [passwordRep, setPasswordRep] = React.useState("");

    const [error, setError] = React.useState([false, false, false, false, false])

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

    const checkRepPassword = (event) => {
        setPasswordRep(event.target.value)
    }

    const checkName = (event) => {
        setName(event.target.value)
    }

    const checkSurname = (event) => {
        setSurname(event.target.value)
    } 

    return (
      <Container size={750} my={40}>
        <Title ta="center" className={classes.title}>
            Join Us Today!
        </Title>
        <Text c="dimmed" size="sm" ta="center" mt={5}>
          Already have an account?{" "}
          <Anchor size="sm" component="button">
            <Link to="/login">Sign In</Link>
          </Anchor>
        </Text>
  
        <Paper withBorder shadow="md" p={30} mt={30} radius="md">
            <Stepper active={active} onStepClick={setActive} allowNextStepsSelect={false}>
                <Stepper.Step icon={<IconMail style={{ width: rem(18), height: rem(18) }} />} label="First step" description="Provide an email">
                    <TextInput label="Email" error={error[0]} value={email} onChange={checkEmail} placeholder="you@example.com" required />
                </Stepper.Step>
                <Stepper.Step icon={<IconUser style={{ width: rem(18), height: rem(18) }} />} label="Second step" description="Provide personal information">
                    <TextInput label="Name" error={error[1]} value={name} onChange={checkName} placeholder="John" required />
                    <TextInput label="Last Name" error={error[2]} value={surname} onChange={checkSurname} placeholder="Smith" required />  
                </Stepper.Step>
                <Stepper.Step icon={<IconKey style={{ width: rem(18), height: rem(18) }} />} label="Final step" description="Set up password">
                    <PasswordInput label="Password" error={error[3]} value={password} onChange={checkPassword} placeholder="Your password" required mt="md" />
                    <PasswordInput label="Repeat Password" error={error[4]} value={passwordRep} onChange={checkRepPassword} placeholder="Repeat password" required mt="md" />
                </Stepper.Step>
                <Stepper.Completed>
                    <div>
                        <Title ta="center" order={3}>
                            Congratulations! 🎉 <br/> Your account registration is almost complete.
                        </Title>
                        <br/>
                        <Text ta="center">
                            We've securely saved all your information. The final step is to verify your email address.
                        </Text>
                        <Text ta="center">
                        Please check your inbox for a verification link. Once you click on it, your registration will be finalized, and you'll be ready to explore all the exciting features our platform has to offer.
                        </Text>
                    </div>
                </Stepper.Completed>
            </Stepper>
            <Group justify="center" mt="xl">
                {
                    active !== 3 ?
                    <>
                        <Button variant="default" onClick={prevStep}>Back</Button>
                        <Button onClick={nextStep}>Next step</Button>
                    </>
                    :
                    <>
                        <Link to="/"><Button variant="default">Home Page</Button></Link>
                    </>
                }
            </Group>
        </Paper>
      </Container>
    );
  }