import { Text, Slider } from '@mantine/core';
import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';
import { useState } from 'react';
import { useReactFlow } from 'reactflow';
import { Switch } from '@mantine/core';

export default memo((props) => {
  const { data, isConnectable } = props;
  const [val, setVal] = React.useState(20)

  return (
    <>
      <div style={{width: 200, background:"white", border: "1px solid black", borderRadius: 5, color:"black"}}>
        {/* {data.name} */}
        <div className='draggable'>
            <div className='draggable' style={{textAlign: "center"}}>Filter</div>
            {
              data.processed ?
                <img className='draggable' src={data.image} style={{width: "100%", margin: "10px auto"}} alt='mask preview'/>
              :
                <div style={{width:"100%", aspectRatio: "16 / 9", marginTop: 10,color:"white", background: "black", textAlign: "Center"}}>No input...</div>
            }
        </div>
        <div style={{width: "100%", padding: "0 10px 10px 10px", textAlign: "center"}}>
            {
              data.processed &&
              <>
                <Text className='draggable' size="sm">Strength</Text>
                {/* {swap ? "yes" : "no"} */}
                <Slider onChangeEnd={(e)=>{if(data.processed) {props.data.updateFilter(props, props.data.edges, e/100, data.image, props.data.swap);}}} defaultValue={80} style={{zIndex: 10000}} showLabelOnHover/>
                <Switch onChange={(e) => {console.log(e.currentTarget.checked);props.data.updateFilter(props, props.data.edges, data.strength/100, data.image, e.currentTarget.checked)}} label="Swap"/>
              </>
            }
        </div>
      </div>
      <Handle
        type="target"
        position={Position.Left}
        id="a"
        style={{padding: 4, top: 40}}
        isConnectable={isConnectable}
      />
      <Handle
        type="target"
        position={Position.Left}
        id="b"
        style={{padding: 4, bottom: 40, top: "auto"}}
        isConnectable={isConnectable}
      />
      <Handle
        type="source"
        position={Position.Right}
        id="c"
        style={{padding: 4}}
        isConnectable={isConnectable}
      />
    </>
  );
});