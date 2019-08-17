# Backend Manager Places (CRUD)

### Address Api Heroku
- heroku

### URL
/places

#### Method:
`GET`

#### URL Params
Optional: `name=[string]`

#### Success Response:
Code: 200

### URL
/places

#### Method:
`POST`

#### Data Params
Example Values:
```
{
  city: string,
  name: string,
  slug: string,
  state: string
}
```

#### Success Response:
Code: 200

#### Error Response:
Code: 400

### URL
/places/{id}

#### Method:
`GET`

#### Path Variable
Required: `id=[int]`

#### Success Response:
Code: 200

#### Error Response:
Code: 404

### URL
/places/{id}

#### Method:
`PUT`

#### Path Variable
Required: `id=[int]`

#### Data Params
Example Values:
```
{
  city: string,
  name: string,
  slug: string,
  state: string
}
```

#### Success Response:
Code: 200

#### Error Response:
Code: 400, 404