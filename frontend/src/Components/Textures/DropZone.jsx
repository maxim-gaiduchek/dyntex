import { Group, Text, rem, Progress, Box, Button } from '@mantine/core';
import { IconUpload, IconPhoto, IconX } from '@tabler/icons-react';
import { Dropzone } from '@mantine/dropzone';
import { useState } from 'react';
import axios from 'axios';
import { Loader } from '@mantine/core';

export default function DropZone(props) {
  const [file, setFile] = useState(null);
  const [finished, setFinished] = useState(false)
  const [progress, setProgress] = useState(0);

  const getFile = (files) => {
    var file_loc = files[0]

    setFile(file_loc);

    var formData = new FormData();

    const options = {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: (progressEvent) => {
        const { loaded, total } = progressEvent;
        var percentage = Math.floor((loaded * 100) / total);
        setProgress(percentage);
      }
    };

    formData.append("video", file_loc);
    axios.post('http://localhost:8080/api/videos', formData, options)
    .then((res) => {
      setFinished(true)
      setProgress(100);
    });
  }

  return (
    <>
      { file === null ?
        <Dropzone
          onDrop={getFile}
          onReject={(files) => console.log('rejected files', files)}
          maxSize={5 * 1024 ** 2}
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
  );
}