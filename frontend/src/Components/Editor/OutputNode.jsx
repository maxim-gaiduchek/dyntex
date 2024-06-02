import { Button } from '@mantine/core';
import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';
import axios from 'axios';
import PythonUrl from '../../PythonUrl';

export default memo(({ data, isConnectable }) => {
  return (
    <>
      <div style={{width: 200, minHeight: 200, background:"white", border: "1px solid black", borderRadius: 5, color:"black"}}>
        {/* {data.name} */}
        <div style={{textAlign: "center"}}>Output</div>
        <img src={data.image} style={{width: "100%", margin: "10px auto"}} alt='mask preview'/>
        <div style={{width: "100%", paddingBottom: 10, textAlign: "center"}}>
            <Button onClick={async (e) => {
              e.preventDefault();
              console.log(data)
              const link = PythonUrl+"/save"
              try{
                const d = await data.handleDownload(e)

                try{
                  const resp = axios.post(link, { data: d, session_id: data.session_id })
                }catch(err){
                }
                window.open("/download/" + data.session_id, '_blank').focus();
              }catch(err){
              }

            }}>Download</Button>
        </div>
      </div>
      <Handle
        type="target"
        position={Position.Left}
        id="b"
        onConnect={console.log}
        style={{padding: 4}}
        isConnectable={isConnectable}
      />
    </>
  );
});