

function compareOptionValues(a, b) {

  var sA = parseInt( a.value, 36 );  

  var sB = parseInt( b.value, 36 );  

  return sA - sB;

}

 

function compareOptionText(a, b) {

  var sA = parseInt( a.text, 36 );  

  var sB = parseInt( b.text, 36 );  

  return sA - sB;

}

 

function moveDualList( srcList, destList, moveAll ) {

  if (  ( srcList.selectedIndex == -1 ) && ( moveAll == false )   )  {

    return;

  }

 

  newDestList = new Array( destList.options.length );

  var len = 0;

 

  for( len = 0; len < destList.options.length; len++ )   {

    if ( destList.options[ len ] != null )    {

      newDestList[ len ] = new Option( destList.options[ len ].text, destList.options[ len ].value, destList.options[ len ].defaultSelected, destList.options[ len ].selected );

    }

  }

 

  for( var i = 0; i < srcList.options.length; i++ )   {

    if ( srcList.options[i] != null && ( srcList.options[i].selected == true || moveAll ) )    {

       newDestList[ len ] = new Option( srcList.options[i].text, srcList.options[i].value, srcList.options[i].defaultSelected, srcList.options[i].selected );

       len++;

    }

  }

 

  newDestList.sort( compareOptionValues );   // BY VALUES

  for ( var j = 0; j < newDestList.length; j++ )   {

    if ( newDestList[ j ] != null )    {

      destList.options[ j ] = newDestList[ j ];

    }

  }

 

  for( var i = srcList.options.length - 1; i >= 0; i-- )   {

    if ( srcList.options[i] != null && ( srcList.options[i].selected == true || moveAll ) )    {

       srcList.options[i]       = null;

    }

  }

}


