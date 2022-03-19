import namor from 'namor'

const range = len => {
  const arr = []
  for (let i = 0; i < len; i++) {
    arr.push(i)
  }
  return arr
}

const newPerson = () => {
  const statusChance = Math.random()
  return {
    person: namor.generate({ words: 1, numbers: 0 }),
    //date: Math.floor(Math.random() * 1647643282),
    date: new Date(+(new Date()) - Math.floor(Math.random()*10000000000)),
    location: Math.floor(Math.random() * 100),
  }
}


export default function makeData(...lens) {
  const makeDataLevel = (depth = 0) => {
    const len = lens[depth]
    return range(len).map(d => {
      return {
        ...newPerson(),
        subRows: lens[depth + 1] ? makeDataLevel(depth + 1) : undefined,
      }
    })
  }

  return makeDataLevel()
}
