import React from 'react';
import { ClipLoader } from 'react-spinners'; // Vamos usar o 'ClipLoader'

// Este é um componente wrapper para centralizar o spinner na tela
function LoadingSpinner() {
  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      width: '100%',
      height: '50vh' // Usa 50% da altura da tela
    }}>
      <ClipLoader
        color={"#007bff"} // Você pode escolher qualquer cor
        loading={true}
        size={50} // Tamanho do spinner
      />
    </div>
  );
}

export default LoadingSpinner;