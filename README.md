{
  "iss": "",
  "aud": "google",
  "typ": "savetowallet",
  "iat": 1700000000,
  "origins": ["http://localhost"],
  "payload": {
    "genericClasses": [
      {
        "id": "1",
        "classTemplateInfo": {
          "cardTemplateOverride": {
            "cardRowTemplateInfos": [
              {
                "twoItems": {
                  "startItem": {
                    "firstValue": {
                      "fields": [
                        {
                          "fieldPath": "object.textModulesData['cardholder']"
                        }
                      ]
                    }
                  },
                  "endItem": {
                    "firstValue": {
                      "fields": [
                        {
                          "fieldPath": "object.textModulesData['member']"
                        }
                      ]
                    }
                  }
                }
              }
            ]
          }
        }
      }
    ],
    "genericObjects": [
      {
        "id": "",
        "classId": "",
        "state": "ACTIVE",

        "cardTitle": {
          "defaultValue": {
            "language": "en-US",
            "value": ""
          }
        },

        "subheader": {
          "defaultValue": {
            "language": "en-US",
            "value": "Cardholder"
          }
        },

        "header": {
          "defaultValue": {
            "language": "en-US",
            "value": "Raja R"
          }
        },

        "textModulesData": [
          {
            "id": "cardholder",
            "header": "Cardholder",
            "body": "Raja R"
          },
          {
            "id": "member",
            "header": "Member Number",
            "body": "1234567890"
          }
        ],

        "heroImage": {
          "sourceUri": {
            "uri": "https://iili.io/BqboiGI.md.png"
          },
          "contentDescription": {
            "defaultValue": {
              "language": "en-US",
              "value": "Hero Image"
            }
          }
        },

        "logo": {
          "sourceUri": {
            "uri": "https://iili.io/BqboiGI.md.png"
          }
        },

        "hexBackgroundColor": "#4285F4",

        "barcode": {
          "type": "QR_CODE",
          "value": "1234567890"
        }
      }
    ]
  }
}
