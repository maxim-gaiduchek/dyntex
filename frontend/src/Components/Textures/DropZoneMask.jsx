import { Group, Text, rem, Progress, Box, Button } from '@mantine/core';
import { IconUpload, IconPhoto, IconX } from '@tabler/icons-react';
import { Dropzone } from '@mantine/dropzone';
import { useState } from 'react';
import axios from 'axios';
import { Loader, TextInput, Textarea } from '@mantine/core';
import { useCookies } from 'react-cookie';
import { notifications } from '@mantine/notifications';
import { IconExclamationCircle } from '@tabler/icons-react';
import CategorySearch from './CategorySearch';
import { IMAGE_MIME_TYPE } from '@mantine/dropzone';
import BaseUrl from '../../BaseUrl'

export default function DropZoneMask(props) {
  const [file, setFile] = useState(null);
  const [filled, setFilled] = useState(false);
  const [tagId, setTagIg] = useState(null);
  const [name, setName] = useState("");
  const [description, setDescription] = useState("")
  const [finished, setFinished] = useState(false)
  const [progress, setProgress] = useState(0);
  const [cookies] = useCookies(['dyntex']);

  const changeTags = (e) => {
    var ids = props.tags.filter((tag) => {return e.includes(tag.emoji + tag.name)});
    let ret = ""
    if(ids.length !== 0){
      ret += ids.map((obj) => obj.id).join(",")
    }
    setTagIg(ret)
  }
  const getFile = (files) => {
    var file_loc = files[0]

    setFile(file_loc);

    var formData = new FormData();

    const options = {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': cookies.token
      },
      onUploadProgress: (progressEvent) => {
        const { loaded, total } = progressEvent;
        var percentage = Math.floor((loaded * 100) / total);
        setProgress(percentage);
      }
    };

    formData.append("mask", file_loc);
    // formData.append("name", name);
    formData.append("description", description);
    console.log(description)
    formData.append("tagIds", tagId)
    formData.append("name", name);
    axios.post(BaseUrl+'/api/masks', formData, options)
    .then((res) => {
      setFinished(true)
      setProgress(100);
    });
  }


  return (
    <>
    {
      filled ?
      <>
        <>
          { file === null ?
            <Dropzone
              onDrop={getFile}
              multiple={false}
              accept={["image/png"]}
              onReject={(files) => {
                notifications.show({
                    title: 'Invalid file',
                    color: 'red',
                    icon: <IconExclamationCircle/>,
                    autoClose: 4000,
                    message: "File size not valid :("
                })
              }}
              maxSize={5 * 102400 ** 2}
              {...props}
            >
              <Group justify="center" gap="xl" mih={220} style={{ pointerEvents: 'none' }}>
                <Dropzone.Accept>
                  <IconUpload
                    style={{ width: rem(52), height: rem(52), color: 'var(--mantine-color-blue-6)' }}
                    stroke={1.5}
                  />
                </Dropzone.Accept>
                <Dropzone.Reject>
                  <IconX
                    style={{ width: rem(52), height: rem(52), color: 'var(--mantine-color-red-6)' }}
                    stroke={1.5}
                  />
                </Dropzone.Reject>
                <Dropzone.Idle>
                  <IconPhoto
                    style={{ width: rem(52), height: rem(52), color: 'var(--mantine-color-dimmed)' }}
                    stroke={1.5}
                  />
                </Dropzone.Idle>
        
                <div>
                  <Text size="xl" inline>
                    Drag images here or click to select files
                  </Text>
                  <Text size="sm" c="dimmed" inline mt={7}>
                    Attach as many files as you like, each file should not exceed 5mb
                  </Text>
                </div>
              </Group>
            </Dropzone>
            :
            <>
            <h2>Uploading file</h2>
            <Group grow wrap='nowrap' preventGrowOverflow={false}>
              <Box>
              <IconPhoto size={50}/>
              </Box>
              <div style={{width: "100%"}}>
                {file.name}
                {!finished ? 
                  <>
                    {progress !== 100 ?
                      <Progress.Root size="xl">
                        <Progress.Section value={progress}>
                          <Progress.Label>{progress}%</Progress.Label>
                        </Progress.Section>
                      </Progress.Root>
                      :
                      <>
                        <Loader type='dots'/>
                      </>
                    }
                  </>
                  :
                  <></>
                }
              </div>
            </Group>
            <br/>
            <Group justify='center'>
              <Button onClick={() => {props.close(); props.fetchData()}} disabled={!finished}>Save</Button>
            </Group>
            </>
          }
        </>
      </>
      :
      <>
      <TextInput label={"Name"} placeholder='Name' value={name} onChange={(e) => setName(e.target.value)}/>
      <br/>
      <CategorySearch tags={props.tags} changeSearch={changeTags} />
      <br/>
      <Textarea label="Description" value={description} onChange={(e) => setDescription(e.target.value)}/>
      <br/>
      <Group justify='center'>
        <Button onClick={() => {setFilled(true)}} disabled={tagId === null || name === ""}>Next</Button>
      </Group>
      </>
    }
    </>
  );
}