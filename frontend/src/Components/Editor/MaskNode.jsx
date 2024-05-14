import React, { memo } from 'react';
import { Handle, Position } from 'reactflow';

export default memo(({ data, isConnectable }) => {
  return (
    <>
      <div>
        {data.name}
      </div>
      <img src={data.image} style={{width: "120px", margin: "0 auto"}} alt='mask preview'/>
      <Handle
        type="source"
        position={Position.Right}
        id="b"
        style={{padding: 4}}
        isConnectable={isConnectable}
      />
    </>
  );
});