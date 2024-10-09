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

import CategorySearch from '../Textures/CategorySearch';
import MaskCard from '../Card/MaskCard';
import DropZone from '../Textures/DropZone';
import BaseUrl from '../../BaseUrl';
import { callApi, getUser } from '../../utils';
export default function MainPage(){

    const [value, setValue] = React.useState("Textures")
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
        'Authorization': "Bearer " + cookies.token
      }
    };

    const changeMode = (mode) => {
      if(value === mode){
        return
      }

      if(mode === "Textures"){
        fetchData()
      }

      if(mode === "Masks"){
        fetchMasks()
      }

      setValue(mode)
    }

    const fetchMasks = async (page = 1) => {
      try{
        const response = await axios.get(BaseUrl+'/api/masks?page='+page);
        setPages(response.data.totalPages)
        setMasks(response.data.masks)
      } catch(e) {
      }
    }

    const fetchData = async (page = 1) => {
      setTextures(null)
      try{
        const response = await axios.get(BaseUrl+'/api/videos?page='+page);
        setPages(response.data.totalPages)
        setTextures(response.data.videos)
      } catch{
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
        // const response = await axios.get(BaseUrl+"/api/users/authenticated", options)
        // setUser(response.data)
        // callApi("/api/users/authenticated", "get", {}, cookies.token).then((response) => {
        //   console.log(response.data)
        //   setUser(response.data)
        // });
        getUser(cookies.token).then((response) => {
          console.log(response.data)
          setUser(response.data)
        });
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
      fetchData()
    },[])

    return (
        <>
          <h2>All Medias</h2>
          {
            value === "Textures" ?
            <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
                  <DropZone tags={tags} fetchData={fetchData} close={close}/>
            </Modal>
            :
            <Modal opened={opened} onClose={close} title="Add Texture" size="lg">
                  <DropZoneMask tags={tags} fetchData={fetchMasks} close={close}/>
            </Modal>
          }
          <Group justify='right'>
            <Grid style={{paddingRight: 10}}>
              {
                value === "Textures" ?
                <Button onClick={open}>Add Texture</Button>
                :
                <Button onClick={open}>Add Mask</Button>
              }
            </Grid>
          </Group>
          <br/>
          <Group justify="space-between" grow>
            <CategorySearch tags={tags} changeSearch={changeSearch} />
            <SegmentedControl onChange={changeMode} value={value} data={['Textures', 'Masks']} />
          </Group>
          <div style={{marginBottom: 20}}>
          </div>
          <Grid style={{overflow: "hidden"}}>
          {
            value === "Textures" 
            ?
            <>
              {
               textures !== null && user !== undefined ?
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
                                <TextureCard liked={user === undefined ? false : (user.likedMedia?.some(media => media.id === texture.id) || false)} texture = {texture}/>
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
            :
            <>
                {
                  masks !== null ?
                  <>
                    {
                      masks.map((mask) => (
                      <Grid.Col span={{xs: 12, md: 6, lg: 4}}>
                          <MaskCard mask={mask}/>
                          {/* <div style={{
                            width: "100%", 
                            height: 300, 
                            backgroundImage: "url("+BaseUrl+"/api/media/previews/"+mask.path+")",
                            backgroundSize: "cover",
                            backgroundPosition: "center"
                            }
                          }>
                          </div> */}
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
            </>
          }
          </Grid>
          <Center maw={"100vw"} h={100}>
            <Pagination mt="sm" total={totalPages} onChange={(e) => {
              if(value === "Textures"){
                fetchData(e)
              }else {
                fetchMasks(e)
              }
            }}/>
          </Center>
        </>
    )
}