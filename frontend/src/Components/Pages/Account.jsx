import { Grid } from '@mantine/core'
import { Title, Text } from '@mantine/core'
import TextureCard from '../Card/TextureCard'
import { useState, useEffect } from 'react'
import { LoadingOverlay } from '@mantine/core'
import axios from 'axios'
import { useNavigate, useParams } from 'react-router-dom'
import { useCookies } from 'react-cookie'
import BaseUrl from '../../BaseUrl'

export default function Account(){
    const [user, setUser] = useState(null)
    let { id } = useParams();
    const navigate = useNavigate()
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);

    const options = {
      headers: {
        'Authorization': cookies.token
      }
    };

    const fetchData = async () => {
      try{
        let url;
        if(id === "me"){
          url = BaseUrl+"/api/users/authenticated"
        }else{
          url = BaseUrl+"/api/users/"+id
        }
        const response = await axios.get(url, options)
        setUser(response.data)
      }catch(e){
        console.log(e)
        navigate("/notfound")
      }
    }

    useEffect(() => {
      fetchData()
    },[])

    return (
        <>
          {
            user !== null ?
            <>
              <Grid style={{marginTop: 50}}>
                <Grid.Col span={3}>
                    <div className='avatar' style={{
                        aspectRatio: "1/1",
                        width: "100%", 
                        backgroundColor: "red",
                        backgroundImage: "url(https://avatars.githubusercontent.com/u/58140020?v=4)",
                        backgroundSize: "cover",
                        backgroundPosition: "center",
                        marginBottom: 20
                    }}>
                    </div>
                </Grid.Col>
                <Grid.Col span={9}>
                    <Title order={3}>{user.name}</Title>
                    <Text c="dimmed" size="xs">{user.email}</Text>
                    Role: {user.role} <br/>
                    Media Uploaded: {user.createdMedia.length}
                    <br/>
                </Grid.Col>
            </Grid>
            <br/>
            <Title order={3}>Uploaded Textures:</Title>
            {/* <Grid className='textureGrid'>
                {
                    user.textures.map((texture) => (
                        <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                            <TextureCard texture = {texture}/>
                        </Grid.Col>
                    ))
                }
            </Grid> */}
            </>
            :
            <div style={{width: "100%", height: "calc(100vh - 20px)", marginTop: 5}}>
              <LoadingOverlay visible={true} zIndex={1000} style={{marginTop: 5}} overlayProps={{ radius: "sm", blur: 2 }}/>
            </div>
          }
        </>
    )
}