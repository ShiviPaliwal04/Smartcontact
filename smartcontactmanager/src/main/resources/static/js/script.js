console.log("Script file");
const toggleSidebar = () => {
if($(".sidebar").is(":visible")){
$(".sidebar").css("display","none");
$(".content").css("margin-left","0%");
}else{
$(".sidebar").css("display","block");
$(".content").css("margin-left","20%");
}
};
const search=()=>{
//console.log("searching......");
let query=$("#search-input").val();
if(query==""){
$(".search-result").hide();
}else{
console.log(query);
let url=`http://localhost:8080/search/${query}`;
fetch(url).then((response)=>{
return response.json();
}).then((data)=>{
//console.log(data);
let text=`<div class='list-group'>`;
data.forEach((contact)=>{
text+=`<a href="/user/${contact.cId}/contact" class='list-group-item list-group-item-action'>${contact.name}</a>`
});
text+=`</div>`;
$(".search-result").html(text);
$(".search-result").show();
}); 
}
};

const paymentStart=()=>{
console.log("payment started");
let amount=$("#payment_field").val()
console.log(amount);
if(amount=="" || amount==null){
alert("Amount is required !!");
return
}

$.ajax(
{
url:'/user/create_order',
data:{amount:amount},
contentType:'application/json',
type:'POST',
dataType:'json',
success:function(response){

},

error:function(error){
console.log(error)
alert("Something went wrong .. !!")
},

});

};