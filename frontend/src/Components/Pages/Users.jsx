import { Grid } from "@mantine/core"
import { useState, useEffect } from "react"
import { UserInfoAction } from "../Card/UserInfoAction"
import axios from "axios"
import BaseUrl from '../../BaseUrl'

export default function Users() {
    const [users, setUsers] = useState([])

    const fetchData = async () => {
        try{
            const response = await axios.get(BaseUrl+"/api/users")
            console.log(response)
            setUsers(response.data.userAccounts) 
        } catch(e){}
    }

    useEffect(()=>{
        fetchData()
    },[])

    return (
        <h2>
            All Dyntex Users
            <br/>
            <br/>
            <Grid>
                {
                    users.map((user) => (
                        <Grid.Col key={user.id} span={{xs: 12, md: 4}}>
                            <UserInfoAction user={user}/>
                        </Grid.Col>
                    ))
                }
            </Grid>
        </h2>
    )
}