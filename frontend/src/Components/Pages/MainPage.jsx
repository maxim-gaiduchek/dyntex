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

import CategorySearch from '../Textures/CategorySearch';
import DropZone from '../Textures/DropZone';

export default function MainPage(){

    const [value, setValue] = React.useState("All")
    const [textures, setTextures] = React.useState(null)
    const [opened, { open, close }] = useDisclosure(false);
    const [tags, setTags] = React.useState([])
    const [lastTags, setLastTags] = React.useState([])
    const [totalPages, setPages] = React.useState(0)
    const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
    const [user, setUser] = React.useState(undefined)
    const navigate = useNavigate()

    const options = {
      headers: {
        'Authorization': cookies.token
      }
    };

    const fetchData = async (page = 1) => {
      setTextures(null)
      try{
        const response = await axios.get('http://localhost:8080/api/videos?page='+page);
        setPages(response.data.totalPages)
        setTextures(response.data.videos)
      } catch{
      }
    }

    const fetchTags = async () => {
      try{
        const response = await axios.get('http://localhost:8080/api/tags')
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
        const response = await axios.get("http://localhost:8080/api/users/authenticated", options)
        setUser(response.data)
      }catch(e){
        //very very bad and stupid =)
        console.log(e)
        // removeCookie("token")
        // navigate("/login")
      }

    }
    const changeSearch = async (values) => {
      if(lastTags.length === values.length && lastTags.every((value, index) => value === values[index])){
        return
      }
      setLastTags(values)
      setTextures(null)
      var ids = tags.filter((tag) => {return values.includes(tag.emoji + tag.name)});
      var url = "http://localhost:8080/api/videos"
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
      fetchData()
    },[])

    return (
        <>
          <h2>DYNTEX))</h2>
          <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
            <DropZone tags={tags} fetchData={fetchData} close={close}/>
          </Modal>
          <Group justify='right'>
            <Grid style={{paddingRight: 10}}>
              <Button onClick={open}>Add Texture</Button>
            </Grid>
          </Group>
          <br/>
          <Group justify="space-between" grow>
            <CategorySearch tags={tags} changeSearch={changeSearch} />
            <SegmentedControl onChange={setValue} value={value} data={['All', 'Texture', 'Mask']} />
          </Group>
          <div style={{marginBottom: 20}}>
          </div>
          <Grid style={{overflow: "hidden"}}>
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
                              <TextureCard texture = {texture}/>
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
          </Grid>
          <Center maw={"100vw"} h={100}>
            <Pagination mt="sm" total={totalPages} onChange={(e) => {
              fetchData(e)
            }}/>
          </Center>
        </>
    )
}