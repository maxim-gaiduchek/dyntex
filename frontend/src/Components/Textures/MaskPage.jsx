import TextureCard from '../Card/TextureCard'
import { Grid } from '@mantine/core'
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

export default function MaskPage(){

    const [textures, setTextures] = React.useState(null)
    const [opened, { open, close }] = useDisclosure(false);
    const [tags, setTags] = React.useState([])
    const [lastTags, setLastTags] = React.useState([])
    const [totalPages, setPages] = React.useState(0)
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
    const [user, setUser] = React.useState(undefined)
    const [masks, setMasks] = React.useState(null)
    const navigate = useNavigate()

    const options = {
      headers: {
        'Authorization': cookies.token
      }
    };

    const fetchMasks = async (page = 1) => {
      try{
        const response = await axios.get(BaseUrl+'/api/masks?page='+page);
        setPages(response.data.totalPages)
        setMasks(response.data.masks)
      } catch(e) {
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

    const checkLogged = async () => {
      if(cookies.token === undefined){
        navigate("/login")
      }

      try{
        const response = await axios.get(BaseUrl+"/api/users/authenticated", options)
        setUser(response.data)
      }catch(e){
        //very very bad and stupid =)
        console.log(e)
        removeCookie("token")
        navigate("/login")
      }

    }
    const changeSearch = async (values) => {
      if(lastTags.length === values.length && lastTags.every((value, index) => value === values[index])){
        return
      }
      setLastTags(values)
      setTextures(null)
      var ids = tags.filter((tag) => {return values.includes(tag.emoji + tag.name)});
      var url = BaseUrl+"/api/videos"
      if(ids.length !== 0){
        url += "?tags=" + ids.map((obj) => obj.id).join(",")
      }
      const response = await axios.get(url);
      setPages(response.data.totalPages)
      setTextures(response.data.videos)
    }

    useEffect(() => {
      checkLogged()
      fetchTags()
      fetchMasks()
    },[])

    return (
        <>
          <h2>All Medias</h2>
          <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
                <DropZoneMask tags={tags} fetchData={fetchMasks} close={close}/>
        </Modal>
          <Group justify='right'>
              <Button onClick={open}>Add Mask</Button>
          </Group>
          <br/>

          <div style={{marginBottom: 20}}>
          </div>
          <Grid style={{overflow: "hidden"}}>
          {
            masks !== null ?
            <>
              {
                masks.map((mask) => (
                <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                    <Link to={"/mask/" + mask.id}>
                      <div style={{
                        width: "100%", 
                        height: 300, 
                        backgroundImage: "url("+BaseUrl+"/api/media/previews/"+mask.path+")",
                        backgroundSize: "cover",
                        backgroundPosition: "center"
                        }
                      }>
                      </div>
                    </Link>
                </Grid.Col>
                ))
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
          </Grid>
          <Center maw={"100vw"} h={100}>
            <Pagination mt="sm" total={totalPages} onChange={(e) => {
                fetchMasks(e)
              
            }}/>
          </Center>
        </>
    )
}