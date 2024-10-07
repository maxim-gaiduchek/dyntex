import TextureCard from '../Card/TextureCard'
import { Grid, TextInput } from '@mantine/core'
import { Pagination } from '@mantine/core';
import { Center, Group } from '@mantine/core';
import { SegmentedControl, Modal, Button } from '@mantine/core';
import React from 'react';
import { useDisclosure } from '@mantine/hooks';
import { useEffect } from 'react';
import { Skeleton } from '@mantine/core';
import axios from 'axios';
import { useCookies } from 'react-cookie';
import { useNavigate } from 'react-router-dom';
import DropZoneMask from '../Textures/DropZoneMask';
import { Link } from 'react-router-dom';
import BaseUrl from '../../BaseUrl';
import CategorySearch from '../Textures/CategorySearch';
import DropZone from '../Textures/DropZone';

export default function Favourites(){

    const [value, setValue] = React.useState("Textures")
    const [textures, setTextures] = React.useState(null)
    const [opened, { open, close }] = useDisclosure(false);
    const [tags, setTags] = React.useState([])
    const [lastTags, setLastTags] = React.useState([])
    const [totalPages, setPages] = React.useState(0)
    const [search, setSearch] = React.useState("")
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
    const [user, setUser] = React.useState(undefined)
    const navigate = useNavigate()

    const options = {
      headers: {
        'Authorization': "Bearer " + cookies.token
      }
    };

    const debounce = (func, delay) => {
        let timer;
        return function (...args) {
          const context = this;
          clearTimeout(timer);
          timer = setTimeout(() => func.apply(context, args), delay);
        };
      };


    const checkLogged = async () => {
      if(cookies.token === undefined){
        navigate("/login")
      }

      try{
        const response = await axios.get(BaseUrl+"/api/users/authenticated", options)
        setUser(response.data)
        setTextures(response.data.likedMedia)
      }catch(e){
        //very very bad and stupid =)
        console.log(e)
        removeCookie("token")
        navigate("/login")
      }

    }


    const fetchTags = async () => {
      try{
        const response = await axios.get(BaseUrl+'/api/tags')
        response.data.tags.forEach((d) => {
          d.value = d.name
        })
        setTags(response.data.tags)
      } catch(e){
      }
    }

    useEffect(() => {
      checkLogged()
      fetchTags()
    },[])

    return (
        <>
          <h2>Textures</h2>
          <div style={{marginBottom: 20}}>
          </div>
          <Grid style={{overflow: "hidden"}}>
          <>
              {
               textures !== null ?
               <>
                 {
                   textures.length === 0 ?
                   <Grid.Col span={12}>
                     <Group justify='center' grow>
                       <h2 style={{textAlign: "center"}}>No textures yet.</h2>
                     </Group>
                   </Grid.Col>
                   :
                   <>
                     {
                         textures.map((texture) => (
                             <Grid.Col key={texture.title} span={{xs: 12, md: 6, lg: 4}}>
                                <TextureCard liked={true} texture = {texture}/>
                             </Grid.Col>
                         ))
                     }
                   </>
                 }
               </>
               :
               <>
                 <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                     <Skeleton visible={true} width={"100%"} height={250}/>
                 </Grid.Col>
                 <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                     <Skeleton visible={true} width={"100%"} height={250}/>
                 </Grid.Col>
                 <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                     <Skeleton visible={true} width={"100%"} height={250}/>
                 </Grid.Col>
               </> 
              }
            </>
          </Grid>
        </>
    )
}